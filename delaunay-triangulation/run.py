import sys

from src.main.py.app import App


major_version = 3
minor_version = 10
if sys.version_info < (major_version, minor_version):
    raise SystemExit(f"This project requires Python version {major_version}.{minor_version} or above.")

App().run()
