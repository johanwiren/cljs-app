{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
  buildInputs = [
    awscli
    clojure-lsp
    nodejs_24
  ];
}
