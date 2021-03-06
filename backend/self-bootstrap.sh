#!/bin/bash
set -e

TIMEFORMAT='%3R'

WYBY=$WYVERN_HOME/bin/wyby

if [ ! -f boot.js ]; then
    echo "Could not find boot.js"
    echo "Did you run ./bootstrap.sh first?"
    exit 1
fi

(
cd src/
rm -f main_js.wyb
$WYBY main_js.wyv
)
echo "Self-bootstrapping"
echo -n "time: "
time node boot.js src/main_js.wyb > selfboot.js || rm selfboot.js
echo "Next boot"
echo -n "time: "
time node selfboot.js src/main_js.wyb > nextboot.js || rm selfboot.js nextboot.js
mv boot.js boot.js.old
mv nextboot.js boot.js
rm selfboot.js
