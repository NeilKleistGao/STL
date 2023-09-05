let print_res res = Printf.printf "res: %i\n" res;;

let program = Lang.Add (Lang.Lit(42), Lang.Lit(0)) in
  let res = Interpreter.eval program in
    print_res res;;

let program = Lang.App (Lang.Fun("x", Lang.Add(Lang.Var("x"), Lang.Lit(1))), Lang.Lit(0)) in
  let res = Interpreter.eval program in
    print_res res;;

let program = Lang.TST (Lang.Les(Lang.Lit(1), Lang.Lit(2)),
  Lang.Lit(-1),
  Lang.TST(Lang.Eqn(Lang.Lit(1), Lang.Lit(2)),
    Lang.Lit(0), Lang.Lit(1))) in
  let res = Interpreter.eval program in
    print_res res;;
  
