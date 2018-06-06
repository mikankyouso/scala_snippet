object worksheet {
  (1 to 5).flatMap(x => (2 to 6).map(x * _)).sum
  (for(x <- 1 to 5; y <- 2 to 6) yield  x * y).sum
}