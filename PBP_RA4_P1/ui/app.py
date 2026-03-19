import tkinter as tk
from tkinter import messagebox

from ui.styles import COLORS, FONTS
from ui.components import StyledButton, StyledEntry, SortableHeader, EditDialog
from config import APP_TITLE, APP_WIDTH, APP_HEIGHT
from database import (
    get_session, obtenir_jugadors,
    afegir_jugador, eliminar_jugador, actualitzar_jugador
)
from export import exportar_csv
from models import Jugador


class App(tk.Tk):
    """
    Classe principal de l'aplicació.
    Gestiona la UI.
    """
    def __init__(self):
        super().__init__()
        self.session     = get_session()
        self._ordre_camp = "nom"
        self._ordre_asc  = True
        self._cerca_id   = None

        self.title(APP_TITLE)
        self.geometry(f"{APP_WIDTH}x{APP_HEIGHT}")
        self.configure(bg=COLORS["bg_primary"])
        self.resizable(True, True)

        self._build_ui()
        self._actualitzar_llista()

    # ── Construcció UI ──────────────────────────────────────────────

    def _build_ui(self):
        """
        Construeix la UI.
        """
        self._build_header()
        self._build_search_bar()
        self._build_list_panel()
        self._build_form_panel()
        self._build_status_bar()

    # Construeix el header amb botons d'ordenació
    def _build_header(self):
        tk.Label(
            self, text="⚔️  Clash — Inventari de Jugadors",
            font=FONTS["title"], bg=COLORS["bg_primary"], fg=COLORS["gold"]
        ).pack(pady=(16, 2))

    # Construeix la barra de cerca
    def _build_search_bar(self):
        bar = tk.Frame(self, bg=COLORS["bg_primary"])
        bar.pack(fill="x", padx=20, pady=(4, 0))

        tk.Label(
            bar, text="🔍", font=FONTS["body"],
            bg=COLORS["bg_primary"], fg=COLORS["text_secondary"]
        ).pack(side="left", padx=(0, 6))

        self.entry_cerca = StyledEntry(bar, placeholder="Cerca per nom, copes o gemes…", width=40)
        self.entry_cerca.pack(side="left")
        self.entry_cerca.bind("<KeyRelease>", self._on_cerca_keyrelease)

        StyledButton(
            bar, "✕", command=self._netejar_cerca, variant="danger"
        ).pack(side="left", padx=(6, 0))

    def _build_list_panel(self):
        panel = tk.Frame(self, bg=COLORS["bg_secondary"], padx=10, pady=10)
        panel.pack(fill="both", expand=True, padx=20, pady=8)

        self.header = SortableHeader(panel, on_sort_callback=self._on_sort)
        self.header.pack(fill="x", pady=(0, 2))

        list_frame = tk.Frame(panel, bg=COLORS["bg_card"])
        list_frame.pack(fill="both", expand=True)

        scrollbar = tk.Scrollbar(list_frame)
        scrollbar.pack(side="right", fill="y")

        self.llista = tk.Listbox(
            list_frame,
            yscrollcommand=scrollbar.set,
            font=FONTS["mono"],
            bg=COLORS["bg_card"], fg=COLORS["text_primary"],
            selectbackground=COLORS["accent"], selectforeground="white",
            relief="flat", borderwidth=0, activestyle="none",
        )
        self.llista.pack(fill="both", expand=True)
        scrollbar.config(command=self.llista.yview)

        self.llista.bind("<Double-Button-1>", self._editar)

    def _build_form_panel(self):
        panel = tk.Frame(self, bg=COLORS["bg_secondary"], padx=16, pady=10)
        panel.pack(fill="x", padx=20, pady=(0, 4))

        tk.Label(
            panel, text="Nou jugador",
            font=FONTS["heading"], bg=COLORS["bg_secondary"],
            fg=COLORS["text_primary"]
        ).grid(row=0, column=0, columnspan=4, sticky="w", pady=(0, 6))

        self.entry_nom   = StyledEntry(panel, placeholder="Nom",   width=20)
        self.entry_copes = StyledEntry(panel, placeholder="Copes", width=10)
        self.entry_gemes = StyledEntry(panel, placeholder="Gemes", width=10)

        self.entry_nom.grid  (row=1, column=0, padx=(0, 8))
        self.entry_copes.grid(row=1, column=1, padx=(0, 8))
        self.entry_gemes.grid(row=1, column=2, padx=(0, 8))

        btn_frame = tk.Frame(panel, bg=COLORS["bg_secondary"])
        btn_frame.grid(row=2, column=0, columnspan=4, pady=(8, 0), sticky="w")

        StyledButton(btn_frame, "➕ Afegir",       command=self._afegir,       variant="primary").pack(side="left", padx=(0, 8))
        StyledButton(btn_frame, "✏️ Editar",        command=self._editar,       variant="primary").pack(side="left", padx=(0, 8))
        StyledButton(btn_frame, "🗑 Eliminar",      command=self._eliminar,     variant="danger" ).pack(side="left", padx=(0, 8))
        StyledButton(btn_frame, "📥 Exportar CSV",  command=self._exportar_csv, variant="primary").pack(side="left")

    def _build_status_bar(self):
        self.status_var = tk.StringVar(value="")
        tk.Label(
            self, textvariable=self.status_var,
            font=FONTS["small"], bg=COLORS["bg_primary"],
            fg=COLORS["text_secondary"], anchor="w"
        ).pack(fill="x", padx=22, pady=(0, 8))

    # ── Cerca i ordenació ───────────────────────────────────────────

    def _on_cerca_keyrelease(self, _=None):
        if self._cerca_id:
            self.after_cancel(self._cerca_id)
        self._cerca_id = self.after(300, self._actualitzar_llista)

    def _netejar_cerca(self):
        self.entry_cerca.delete(0, tk.END)
        self.entry_cerca._restore_placeholder()
        self._actualitzar_llista()

    def _on_sort(self, camp: str, asc: bool):
        self._ordre_camp = camp
        self._ordre_asc  = asc
        self._actualitzar_llista()

    # ── CRUD ────────────────────────────────────────────────────────

    def _actualitzar_llista(self):
        """
        Actualitza la llista de jugadors mostrada a la interfície.
        """
        cerca = self.entry_cerca.get_value()
        jugadors = obtenir_jugadors(
            self.session,
            cerca=cerca,
            ordre_camp=self._ordre_camp,
            ordre_asc=self._ordre_asc,
        )
        self.llista.delete(0, tk.END)
        for j in jugadors:
            self.llista.insert(tk.END, f"  {j}")

        total = len(jugadors)
        filtre = f" · filtre: «{cerca}»" if cerca else ""
        self.status_var.set(
            f"{total} jugador{'s' if total != 1 else ''} trobat{'s' if total != 1 else ''}{filtre}"
        )

    def _afegir(self):
        nom   = self.entry_nom.get_value()
        copes = self.entry_copes.get_value()
        gemes = self.entry_gemes.get_value()

        if not nom:
            messagebox.showwarning("Camp buit", "El nom és obligatori.")
            return
        try:
            copes = int(copes) if copes else 0
            gemes = int(gemes) if gemes else 0
        except ValueError:
            messagebox.showerror("Error", "Copes i Gemes han de ser números enters.")
            return

        try:
            afegir_jugador(self.session, nom, copes, gemes)
            self._actualitzar_llista()
            for entry in (self.entry_nom, self.entry_copes, self.entry_gemes):
                entry.delete(0, tk.END)
                entry._restore_placeholder()
        except ValueError as e:
            messagebox.showerror("Error", str(e))

    def _editar(self, _event=None):
        seleccio = self.llista.curselection()
        if not seleccio:
            messagebox.showwarning("Atenció", "Selecciona un jugador per editar.")
            return

        text = self.llista.get(seleccio[0]).strip()
        try:
            jugador_id = int(text.split("│")[0].strip())
        except (ValueError, IndexError):
            messagebox.showerror("Error", "No s'ha pogut llegir l'ID del jugador.")
            return

        jugador = self.session.get(Jugador, jugador_id)
        if not jugador:
            messagebox.showerror("Error", "Jugador no trobat a la base de dades.")
            return

        EditDialog(self, jugador, on_save_callback=self._guardar_edicio)

    def _guardar_edicio(self, jugador_id: int, valors: dict):
        try:
            actualitzar_jugador(self.session, jugador_id, **valors)
            self._actualitzar_llista()
        except ValueError as e:
            messagebox.showerror("Error", str(e))

    def _eliminar(self):
        seleccio = self.llista.curselection()
        if not seleccio:
            messagebox.showwarning("Atenció", "Selecciona un jugador per eliminar.")
            return

        text = self.llista.get(seleccio[0]).strip()
        try:
            jugador_id = int(text.split("│")[0].strip())
        except (ValueError, IndexError):
            messagebox.showerror("Error", "No s'ha pogut llegir l'ID del jugador.")
            return

        if messagebox.askyesno("Confirmar", "Eliminar el jugador seleccionat?"):
            try:
                eliminar_jugador(self.session, jugador_id)
                self._actualitzar_llista()
            except ValueError as e:
                messagebox.showerror("Error", str(e))

    def _exportar_csv(self):
        cerca = self.entry_cerca.get_value()
        jugadors = obtenir_jugadors(
            self.session,
            cerca=cerca,
            ordre_camp=self._ordre_camp,
            ordre_asc=self._ordre_asc,
        )
        exportar_csv(jugadors)