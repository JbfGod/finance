#!/bin/bash

set -e -u

base_dir="$(cd $(dirname $0) && pwd)"

deploy_group=${DEPLOY_GROUP:-'prod'}
ssh_password=${SSH_PASSWORD:-'ssh-password'}
ssh_private_key=${SSH_PRIVATE_KEY:-'~/.ssh/cscec3b_id_rsa'}

echo "Start deploy..."

echo "Setup group: ${deploy_group}"

export ANSIBLE_CONFIG="${base_dir}/ansible.cfg"
ansible-playbook \
  -e deploy_group=${deploy_group} \
  -e ssh_password=${ssh_password} \
  -e ssh_private_key=${ssh_private_key} \
  -i "${base_dir}/hosts.ini" \
  "${base_dir}/task.yml"

echo "Deploy done."
