package stl

sealed class Expr {
  open fun subst(name: String, value: Expr): Expr = this
}
interface BoolIR {
  fun substBool(name: String, value: Expr): BoolIR
}

data class Eqn(val lhs: Expr, val rhs: Expr): Expr(), BoolIR {
  override fun substBool(name: String, value: Expr): BoolIR =
    Eqn(lhs.subst(name, value), rhs.subst(name, value))
}
data class Les(val lhs: Expr, val rhs: Expr): Expr(), BoolIR {
  override fun substBool(name: String, value: Expr): BoolIR =
    Les(lhs.subst(name, value), rhs.subst(name, value))
}

data class Lit(val value: Int): Expr() {
  override fun subst(name: String, value: Expr): Expr = this
}
data class Var(val name: String): Expr() {
  override fun subst(name: String, value: Expr): Expr =
    if (this.name == name) value else this
}
data class Add(val lhs: Expr, val rhs: Expr): Expr() {
  override fun subst(name: String, value: Expr): Expr =
    Add(lhs.subst(name, value), rhs.subst(name, value))
}
data class Fun(val arg: String, val body: Expr): Expr() {
  override fun subst(name: String, value: Expr): Expr =
    if (arg != name) Fun(arg, body.subst(name, value)) else this
}
data class App(val f: Expr, val a: Expr): Expr() {
  override fun subst(name: String, value: Expr): Expr =
    App(f.subst(name, value), a.subst(name, value))
}
data class Tst(val cond: BoolIR, val csq: Expr, val alt: Expr): Expr() {
  override fun subst(name: String, value: Expr): Expr =
    Tst(cond.substBool(name, value), csq.subst(name, value), alt.subst(name, value))
}
