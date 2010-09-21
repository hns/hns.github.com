set terminal svg enhanced size 600 380 fname "Helvetica" fsize 11
set output "results/graphs/no-alloc.svg"
set title "no memory allocation (-n 50000 -c 50)"
set size 0.97,0.97
set grid y
set key left top
set xlabel "requests"
set ylabel "response time (ms)"
plot "results/no-alloc/node.dat" using 9 smooth sbezier with lines title "node", \
     "results/no-alloc/ringo.dat" using 9 smooth sbezier with lines title "ringo"
