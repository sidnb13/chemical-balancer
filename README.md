# chemical-balancer

## Overview

>A utility to balance common chemical equations based on standardized input.

Uses the Apache Commons Math library to solve a matrix-vector equation which represents the chemical equation in a more algebraic sense. 
Modified my parsing algorithm using Stack which uses a regular expression to match certain parts of a formula and places each element with its frequency in a map (see other project).
Also provides the molar mass of each compound or formula unit involved. This is to aid in solving stoichiometry problems. See an example below.

Unbalanced: `O2 + H2 --> H2O`

Balanced: `O2 + 2H2 --> H2O`

## Writeup

See `writeup.pdf` for an example demonstrating the mathematical methods used.
