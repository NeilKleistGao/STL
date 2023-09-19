package stl

sealed class Type() {}

object IntType: Type() {}
object BoolType: Type() {}
object NothingType: Type() {}
data class FunType(val arg: Type, val res: Type): Type() {}
data class TypeVar(val id: Int): Type() {}
