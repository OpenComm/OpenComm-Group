# Makefile for latex paper (conference)

all: paper.pdf

paper.pdf: paper.tex paper.bib authblk.sty usenix.sty
	pdflatex paper.tex
	bibtex paper
	pdflatex paper.tex
	pdflatex paper.tex

clean:
	rm -rf paper.aux paper.bbl paper.blg paper.log paper.pdf

