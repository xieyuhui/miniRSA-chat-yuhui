all:	compile
compile:	run
	ant create_run_jar
run:	
	java -jar cracker.jar 451 2623
clean:	
	rm	cracker.jar
