all: compile run

compile:
	@mkdir -p bin
	javac -d bin -cp lib/* src/*.java

run:
	java -cp bin:lib/* Main

clean:
	rm -rf bin/
