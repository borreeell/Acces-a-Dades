from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import declarative_base

Base = declarative_base()

class Jugador(Base):
    """
    Classe que representa un jugador a la base de dades.
    """
    __tablename__ = "jugadors"

    id     = Column(Integer, primary_key=True, autoincrement=True)
    nom    = Column(String(100), nullable=False)
    copes  = Column(Integer, default=0)
    gemes  = Column(Integer, default=0)
    nivell = Column(Integer, default=1)
    oro    = Column(Integer, default=100)

    def __init__(self, nom: str, copes: int = 0, gemes: int = 0):
        """
        Inicialitza un nou jugador.
        """
        self.nom   = nom
        self.copes = copes
        self.gemes = gemes

    def __repr__(self):
        """
        Retorna una representació en cadena del jugador.
        """
        return (f"{self.id:>4} │ {self.nom:<20} │ "f"Copes: {self.copes:>4} │ Gemes: {self.gemes:>4} │ "f"Nivell: {self.nivell} │ Or: {self.oro}")