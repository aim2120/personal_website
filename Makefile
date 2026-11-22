PYTHON := .venv/bin/python
PIP := .venv/bin/pip

.PHONY: all build clean serve venv

all: build

venv: $(PYTHON)

$(PYTHON):
	python3 -m venv .venv
	$(PIP) install -r requirements.txt

build: venv
	$(PYTHON) build.py

clean:
	rm -rf build

serve: build
	$(PYTHON) -m http.server --directory build 8000
