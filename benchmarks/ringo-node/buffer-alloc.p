set terminal svg enhanced size 600 380 fname "Helvetica" fsize 11
set output "results/graphs/buffer-alloc.svg"
set title "50k buffer allocation/request (-n 50000 -c 50)"
set size 0.97,0.97
set grid y
set key left top
set xlabel "requests"
set ylabel "response time (ms)"
plot "results/buffer-alloc/node.dat" using 9 smooth sbezier with lines title "node", \
     "results/buffer-alloc/ringo.dat" using 9 smooth sbezier with lines title "ringo"
