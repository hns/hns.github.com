set terminal svg enhanced size 600 380 fname "Helvetica" fsize 11
set output "results/graphs/string-alloc.svg"
set title "50k string allocation/request (-n 50000 -c 50)"
set size 0.97,0.97
set grid y
set key left top
set xlabel "requests"
set ylabel "response time (ms)"
plot "results/string-alloc/node.dat" using 9 smooth sbezier with lines title "node", \
     "results/string-alloc/ringo.dat" using 9 smooth sbezier with lines title "ringo"
