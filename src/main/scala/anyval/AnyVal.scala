package anyval

import scala.meta._

import scala.annotation.StaticAnnotation

class AnyVal extends StaticAnnotation {

  inline def apply(defns: Any): Any = meta {

    def isOverride(mod: Mod) = mod match {
      case mod"override" => true
      case _ => false
    }

    def isValParam(mod: Mod) = mod match {
      case mod"valparam" => true
      case _ => false
    }

    def isNonPublic(mod: Mod) = mod match {
      case mod"private" => true
      case mod"protected" => true
      case _ => false
    }

    def isPublicVal(mods: Seq[Mod]) = mods.exists(isValParam) && !mods.exists(isNonPublic)

    def rewriteClass(classDef: Stat): Stat = {
      val q"..$mods class $tname[..$tparams] ..$pmods (...$paramss) extends { ..$earlyInits } with ..$ctorcalls { $slf => ..$stats }" = classDef

      val validValueClass = {
        paramss.size == 1 &&
          paramss.head.size == 1 &&
          isPublicVal(paramss.head.head.mods)
          // TODO if it's a case class, it doesn't need to be a val
      }

      println(s"Is valid value class? $validValueClass")

      val extendsAnyVal = ctorcalls.exists {
        case q"$name()" if name.toString == "AnyVal" => true
        case _ => false
      }

      // TODO check there is only one argument and it is a non-private val
      // TODO add "extends AnyVal" if not present
      
      val hasToStringDefined: Boolean = stats.exists {
        case q"..$mods def $name[..$tparams](...$paramss): $tpe = $expr" if mods.exists(isOverride) && name.toString == "toString" => true
        case other => false
      }

      if (hasToStringDefined)
        // return unchanged
        classDef
      else {
        // TODO add toString method
        classDef
      }
    }

    println("---")
    defns match {
      case both @ q"""{ $cls; $obj }""" => 
        println(s"class: $cls")
        println(s"object: $obj")
        both
      case cc @ q"..$mods class $tname[..$tparams] (...$paramss) extends $template" => 
        rewriteClass(cc)
      case other => 
        println(s"Something unexpected: $other")
        other
    }
  }


}
