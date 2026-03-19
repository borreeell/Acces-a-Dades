"""
Mòdul de gestió de la base de dades.
Proporciona funcions per interactuar amb la base de dades dels jugadors.
"""

from sqlalchemy import create_engine, or_, String
from sqlalchemy.orm import sessionmaker, Session
from config import DATABASE_URL
from models import Base, Jugador

engine = create_engine(DATABASE_URL, pool_pre_ping=True)
Base.metadata.create_all(engine)
SessionLocal = sessionmaker(bind=engine)


def get_session() -> Session:
    """
    Crea i retorna una nova sessió de base de dades.

    Returns:
        Session: Una sessió de SQLAlchemy.
    """
    return SessionLocal()


def obtenir_jugadors(
    session: Session,
    cerca: str = "",
    ordre_camp: str = "nom",
    ordre_asc: bool = True
) -> list[Jugador]:
    """
    Obté una llista de jugadors amb opcions de cerca i ordenació.

    Args:
        session (Session): Sessió de base de dades.
        cerca (str): Terme de cerca (per defecte "").
        ordre_camp (str): Camp per ordenar (per defecte "nom").
        ordre_asc (bool): Ordre ascendent (per defecte True).

    Returns:
        list[Jugador]: Llista de jugadors.
    """
    query = session.query(Jugador)

    if cerca.strip():
        terme = f"%{cerca.strip()}%"
        query = query.filter(
            or_(
                Jugador.nom.ilike(terme),
                Jugador.copes.cast(String).ilike(terme),
                Jugador.gemes.cast(String).ilike(terme),
            )
        )

    camp = getattr(Jugador, ordre_camp, Jugador.nom)
    query = query.order_by(camp.asc() if ordre_asc else camp.desc())

    return query.all()


def afegir_jugador(session: Session, nom: str, copes: int, gemes: int) -> Jugador:
    """
    Afegeix un nou jugador a la base de dades.

    Args:
        session (Session): Sessió de base de dades.
        nom (str): Nom del jugador.
        copes (int): Nombre de copes.
        gemes (int): Nombre de gemes.

    Returns:
        Jugador: El jugador afegit.

    Raises:
        ValueError: Si el nom és buit.
    """
    if not nom or not nom.strip():
        raise ValueError("El nom no pot estar buit.")
    jugador = Jugador(nom=nom.strip(), copes=copes, gemes=gemes)
    session.add(jugador)
    session.commit()
    session.refresh(jugador)
    return jugador


def eliminar_jugador(session: Session, jugador_id: int) -> None:
    """
    Elimina un jugador de la base de dades.

    Args:
        session (Session): Sessió de base de dades.
        jugador_id (int): ID del jugador a eliminar.

    Raises:
        ValueError: Si el jugador no existeix.
    """
    jugador = session.query(Jugador).filter_by(id=jugador_id).first()
    if not jugador:
        raise ValueError(f"Jugador amb id {jugador_id} no trobat.")
    session.delete(jugador)
    session.commit()


def actualitzar_jugador(session: Session, jugador_id: int, **kwargs) -> Jugador:
    """
    Actualitza les dades d'un jugador.

    Args:
        session (Session): Sessió de base de dades.
        jugador_id (int): ID del jugador a actualitzar.
        **kwargs: Camps a actualitzar.

    Returns:
        Jugador: El jugador actualitzat.

    Raises:
        ValueError: Si el jugador no existeix.
    """
    jugador = session.query(Jugador).filter_by(id=jugador_id).first()
    if not jugador:
        raise ValueError(f"Jugador amb id {jugador_id} no trobat.")
    for camp, valor in kwargs.items():
        if hasattr(jugador, camp):
            setattr(jugador, camp, valor)
    session.commit()
    session.refresh(jugador)
    return jugador