import tkinter as tk
from tkinter import messagebox
from ui.styles import COLORS, FONTS

# Components personalitzats per a la UI
class StyledButton(tk.Button):
    """
    Botó personalitzat amb estils i efectes de hover.
    """
    def __init__(self, parent, text, command=None, variant="primary", **kwargs):
        color = COLORS["btn_primary"] if variant == "primary" else COLORS["btn_danger"]
        hover = COLORS["btn_hover"]   if variant == "primary" else "#922b21"
        super().__init__(
            parent, text=text, command=command,
            bg=color, fg=COLORS["text_primary"],
            font=FONTS["heading"], relief="flat",
            cursor="hand2", padx=12, pady=6, **kwargs
        )
        self.default_color = color
        self.hover_color   = hover
        self.bind("<Enter>", lambda e: self.config(bg=self.hover_color))
        self.bind("<Leave>", lambda e: self.config(bg=self.default_color))


class StyledEntry(tk.Entry):
    """
    Entrada de text personalitzada amb placeholder.
    """
    def __init__(self, parent, placeholder="", **kwargs):
        super().__init__(
            parent,
            bg=COLORS["entry_bg"], fg=COLORS["text_secondary"],
            insertbackground=COLORS["text_primary"],
            relief="flat", font=FONTS["body"],
            highlightthickness=1,
            highlightbackground=COLORS["entry_border"],
            highlightcolor=COLORS["accent"],
            **kwargs
        )
        self.placeholder = placeholder
        self._placeholder_active = False
        self._set_placeholder()
        self.bind("<FocusIn>",  self._clear_placeholder)
        self.bind("<FocusOut>", self._restore_placeholder)

    def _set_placeholder(self):
        if not self.get():
            self.insert(0, self.placeholder)
            self.config(fg=COLORS["text_secondary"])
            self._placeholder_active = True

    def _clear_placeholder(self, _=None):
        if self._placeholder_active:
            self.delete(0, tk.END)
            self.config(fg=COLORS["text_primary"])
            self._placeholder_active = False

    def _restore_placeholder(self, _=None):
        if not self.get():
            self._set_placeholder()

    def get_value(self) -> str:
        return "" if self._placeholder_active else self.get()


class SortableHeader(tk.Frame):
    """
    Capçalera ordenable per a la llista de jugadors.
    """
    CAMPS = [
        ("nom",    "Nom",    20),
        ("copes",  "Copes",   8),
        ("gemes",  "Gemes",   8),
        ("nivell", "Nivell",  7),
        ("oro",    "Or",      7),
    ]

    def __init__(self, parent, on_sort_callback, **kwargs):
        super().__init__(parent, bg=COLORS["bg_primary"], **kwargs)
        self._callback   = on_sort_callback
        self._ordre_camp = "nom"
        self._ordre_asc  = True
        self._botons     = {}
        self._build()

    def _build(self):
        for camp, etiqueta, amplada in self.CAMPS:
            btn = tk.Button(
                self, text=f"{etiqueta} ↕",
                font=FONTS["small"], relief="flat",
                bg=COLORS["bg_primary"], fg=COLORS["text_secondary"],
                cursor="hand2", width=amplada,
                command=lambda c=camp: self._ordenar(c)
            )
            btn.pack(side="left", padx=1)
            self._botons[camp] = btn

    def _ordenar(self, camp: str):
        if self._ordre_camp == camp:
            self._ordre_asc = not self._ordre_asc
        else:
            self._ordre_camp = camp
            self._ordre_asc  = True
        self._actualitzar_visual()
        self._callback(self._ordre_camp, self._ordre_asc)

    def _actualitzar_visual(self):
        for camp, btn in self._botons.items():
            _, etiqueta, _ = next(c for c in self.CAMPS if c[0] == camp)
            if camp == self._ordre_camp:
                fletxa = "↑" if self._ordre_asc else "↓"
                btn.config(
                    text=f"{etiqueta} {fletxa}",
                    fg=COLORS["gold"],
                    font=(*FONTS["small"][:2], "bold")
                )
            else:
                btn.config(
                    text=f"{etiqueta} ↕",
                    fg=COLORS["text_secondary"],
                    font=FONTS["small"]
                )


class EditDialog(tk.Toplevel):
    """
    Diàleg modal per editar un jugador.
    """
    def __init__(self, parent, jugador, on_save_callback):
        super().__init__(parent)
        self.jugador  = jugador
        self._on_save = on_save_callback

        self.title(f"Editar jugador — {jugador.nom}")
        self.geometry("340x300")
        self.configure(bg=COLORS["bg_primary"])
        self.resizable(False, False)
        self.transient(parent)
        self.grab_set()

        self._build()

    def _build(self):
        tk.Label(
            self, text="✏️  Editar jugador",
            font=FONTS["heading"], bg=COLORS["bg_primary"],
            fg=COLORS["gold"]
        ).pack(pady=(16, 12))

        form = tk.Frame(self, bg=COLORS["bg_primary"])
        form.pack(padx=24, fill="x")

        camps = [
            ("nom",    "Nom",    str,  self.jugador.nom),
            ("copes",  "Copes",  int,  self.jugador.copes),
            ("gemes",  "Gemes",  int,  self.jugador.gemes),
            ("nivell", "Nivell", int,  self.jugador.nivell),
            ("oro",    "Or",     int,  self.jugador.oro),
        ]

        self._entries = {}
        for i, (camp, etiqueta, tipus, valor) in enumerate(camps):
            tk.Label(
                form, text=etiqueta, width=8, anchor="w",
                font=FONTS["body"], bg=COLORS["bg_primary"],
                fg=COLORS["text_secondary"]
            ).grid(row=i, column=0, pady=4)

            entry = StyledEntry(form, width=22)
            entry.insert(0, str(valor if valor is not None else ""))
            entry._placeholder_active = False
            entry.config(fg=COLORS["text_primary"])
            entry.grid(row=i, column=1, pady=4)
            self._entries[camp] = (entry, tipus)

        btn_frame = tk.Frame(self, bg=COLORS["bg_primary"])
        btn_frame.pack(pady=16)

        StyledButton(btn_frame, "💾 Desar",     command=self._desar,  variant="primary").pack(side="left", padx=(0, 8))
        StyledButton(btn_frame, "✕ Cancel·lar", command=self.destroy, variant="danger" ).pack(side="left")

    def _desar(self):
        valors = {}
        for camp, (entry, tipus) in self._entries.items():
            text = entry.get().strip()
            if not text:
                messagebox.showwarning("Camp buit", f"El camp «{camp}» no pot estar buit.", parent=self)
                return
            try:
                valors[camp] = tipus(text)
            except ValueError:
                messagebox.showerror(
                    "Error",
                    f"El camp «{camp}» ha de ser un valor {'enter' if tipus == int else 'text'}.",
                    parent=self
                )
                return

        self._on_save(self.jugador.id, valors)
        self.destroy()