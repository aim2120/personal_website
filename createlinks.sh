#!/bin/bash
IGNORE=( "about_content.html"
    "art_content.html"
    "blog_content.html"
    "code_content.html"
    "connect_content.html"
    "index_content.html"
    "template.html"
    "createlinks.sh"
    "java"
)
MASTER_DIR=~/Desktop/Personal_Website
SYMLINK_DIR=~/Desktop/annalisemariottini.com
cd $SYMLINK_DIR
for MASTER_F in "$MASTER_DIR"/*
do
    LINK_F=$(basename "$MASTER_F")
    if [ -f "${SYMLINK_DIR}/${LINK_F}" ]; then
        echo "$LINK_F exists"
        rm $LINK_F
    fi
    if [ -d "${SYMLINK_DIR}/${LINK_F}" ]; then
        echo "$LINK_F exists"
        rm -rf $LINK_F
    fi
    if ! [[ ${IGNORE[*]} =~ $LINK_F ]]; then
        echo "creating file ${SYMLINK_DIR}/${LINK_F}"
        ln -s $MASTER_F "${SYMLINK_DIR}/${LINK_F}"
    else
        echo "ignoring file $LINK_F"
    fi
done
