all: run clean

union.cmx: union.ml
	ocamlopt -c union.ml

lang.cmx: lang.ml
	ocamlopt -c lang.ml

interpreter.cmx: interpreter.ml
	ocamlopt -c interpreter.ml

main.cmx: main.ml
	ocamlopt -c main.ml

run: union.cmx lang.cmx interpreter.cmx main.cmx
	ocamlopt -o main union.cmx lang.cmx interpreter.cmx main.cmx

clean:
	rm *.cmx *.cmi *.o
