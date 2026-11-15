all: compile run

compile:
	@mkdir -p bin
	javac -d bin -cp lib/* src/*.java

run:
	java -cp bin:lib/* Main

# runs in development mode
dev: compile
	java -cp bin:lib/* Main -dev

clean:
	rm -rf bin/
