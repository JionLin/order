#!/usr/bin/env bash
# Optional helper for constrained VMs where Docker bridge hairpin/forwarding stalls.
# Safe no-op on normal Docker Desktop / Linux hosts.
set -euo pipefail
if [[ -w /proc/sys/net/bridge/bridge-nf-call-iptables ]]; then
  echo 0 > /proc/sys/net/bridge/bridge-nf-call-iptables || true
fi
if [[ -w /proc/sys/net/ipv4/ip_forward ]]; then
  echo 1 > /proc/sys/net/ipv4/ip_forward || true
fi
