all:
	@echo -n "+ Compiling: "
	@javac *.java 
	@echo "OK"

clean:
	@echo -n "+ Cleaning: "
	@\rm -rf *.class
	@echo "OK"

.PHONY: clean
