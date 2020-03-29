# chemical-balancer
A utility to balance common chemical equations based on standardized input.

Uses the Apache Commons Math library to solve a matrix which represents the chemical equation in a more algebraic sense. 
Modified my parsing algorithm using Stack which uses a regular expression to match certain parts of a formula and places each element with its frequency in a map (see other project).
Also provides the molar mass of each compound or formula unit involved. This is to aid in solving stoichiometry problems.
