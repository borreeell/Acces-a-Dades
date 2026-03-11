from sqlalchemy import create_engine, or_, String
from sqlalchemy.orm import sessionmaker, Session
from config import DATABASE_URL
from models import Base, Jugador

engine = create_engine(DATABASE_URL, pool_pre_ping=True)
Base.metadata.create_all(engine)
SessionLocal = sessionmaker(bind=engine)


def get_session() -> Session:
    return SessionLocal()


def obtenir_jugadors(
    session: Session,
    cerca: str = "",
    ordre_camp: str = "nom",
    ordre_asc: bool = True
) -> list[Jugador]:
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
    if not nom or not nom.strip():
        raise ValueError("El nom no pot estar buit.")
    jugador = Jugador(nom=nom.strip(), copes=copes, gemes=gemes)
    session.add(jugador)
    session.commit()
    session.refresh(jugador)
    return jugador


def eliminar_jugador(session: Session, jugador_id: int) -> None:
    jugador = session.query(Jugador).filter_by(id=jugador_id).first()
    if not jugador:
        raise ValueError(f"Jugador amb id {jugador_id} no trobat.")
    session.delete(jugador)
    session.commit()


def actualitzar_jugador(session: Session, jugador_id: int, **kwargs) -> Jugador:
    jugador = session.query(Jugador).filter_by(id=jugador_id).first()
    if not jugador:
        raise ValueError(f"Jugador amb id {jugador_id} no trobat.")
    for camp, valor in kwargs.items():
        if hasattr(jugador, camp):
            setattr(jugador, camp, valor)
    session.commit()
    session.refresh(jugador)
    return jugador