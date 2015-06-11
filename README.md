# threejs-book

Practice project for going through the 'Learning Three.js' book.

## Usage

From a terminal:

	lein figwheel

Then open `index.html` in a web browser. Live reloading of CLJS, HTML and CSS should work.

If you want to REPL into the running Figwheel process, connect to the nREPL that is already running at port 7888 (it has been started by `lein figwheel`), and then eval the following string in it:

	(use 'figwheel-sidecar.repl-api) (cljs-repl)

Magic should start happening.

## License

Whatever man.
