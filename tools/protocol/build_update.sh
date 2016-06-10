#!/bin/sh

SCRIPT_DIR="$( cd "$( dirname $0 )" && pwd )"
GIT_ROOT="$(git rev-parse --show-toplevel)"

cd $SCRIPT_DIR
sh build_proto.sh $GIT_ROOT/proto/update $GIT_ROOT/framework/update/src/main/java *.proto