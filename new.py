#!/usr/bin/env python3

import distutils.dir_util
import os
import re
import sys


def replace_template_in_file(file_path):
    with open(file_path, "r+") as file:
        content = file.read().replace("Template", sketch_name)
        file.seek(0)
        file.write(content)
        file.truncate()


if len(sys.argv) != 3:
    raise ValueError("Exactly two arguments required: directory name and sketch class name.")
dir_name = sys.argv[1]
sketch_name = sys.argv[2]
if not re.fullmatch(r"[\w-]+", dir_name):
    print(f"Invalid directory name {dir_name}.")
    sys.exit(1)
if not re.fullmatch(r"[a-zA-Z][a-zA-Z0-9]*", sketch_name):
    print(f"Invalid sketch name {sketch_name}.")
    sys.exit(1)
if os.path.exists(dir_name):
    print("A directory with that name already exists!")
    sys.exit(1)

distutils.dir_util.copy_tree("template", dir_name)
os.remove(os.path.join(dir_name, "README.md"))
replace_template_in_file(os.path.join(dir_name, "build.gradle"))
java_dir = os.path.join(dir_name, "src", "main", "java")
template_file = os.path.join(java_dir, "Template.java")
replace_template_in_file(template_file)
os.rename(template_file, os.path.join(java_dir, sketch_name + ".java"))

print(f"Successfully created file://{os.path.abspath(dir_name)}")
