import csv
from datetime import datetime
from tkinter import filedialog, messagebox
from models import Jugador


def exportar_csv(jugadors: list[Jugador]) -> None:
    """
    Exporta la llista de jugadors a un fitxer CSV.

    Mostra un diàleg per seleccionar la ruta del fitxer i exporta les dades.
    """
    if not jugadors:
        messagebox.showwarning("Exportar", "No hi ha jugadors per exportar.")
        return

    # Proposa un nom de fitxer amb data
    nom_defecte = f"jugadors_{datetime.now().strftime('%Y%m%d_%H%M%S')}.csv"
    ruta = filedialog.asksaveasfilename(
        defaultextension=".csv",
        filetypes=[("CSV files", "*.csv"), ("All files", "*.*")],
        initialfile=nom_defecte,
        title="Desa l'exportació CSV",
    )
    if not ruta:
        return  # L'usuari va cancel·lar

    camps = ["id", "nom", "copes", "gemes", "nivell", "oro"]
    try:
        with open(ruta, "w", newline="", encoding="utf-8-sig") as f:  # utf-8-sig per Excel
            writer = csv.DictWriter(f, fieldnames=camps)
            writer.writeheader()
            for j in jugadors:
                writer.writerow({
                    "id":     j.id,
                    "nom":    j.nom,
                    "copes":  j.copes,
                    "gemes":  j.gemes,
                    "nivell": j.nivell,
                    "oro":    j.oro,
                })
        messagebox.showinfo("Exportar", f"Fitxer desat correctament:\n{ruta}")
    except OSError as e:
        messagebox.showerror("Error", f"No s'ha pogut desar el fitxer:\n{e}")