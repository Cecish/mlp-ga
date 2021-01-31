#!/bin/bash

function myfun {
cat << EOF | gnuplot -p
plot "$1" using $2:$3
EOF
}
