all: run clean

lang.cmx: lang.ml
	ocamlopt -c lang.ml

interpreter.cmx: interpreter.ml
	ocamlopt -c interpreter.ml

main.cmx: main.ml
	ocamlopt -c main.ml

run: lang.cmx interpreter.cmx main.cmx
	ocamlopt -o main lang.cmx interpreter.cmx main.cmx

clean:
	rm *.cmx *.cmi *.o
