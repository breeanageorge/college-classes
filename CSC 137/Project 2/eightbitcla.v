`include "cla.v"
module eightbitcla(a, b, cin, sout, cout);
	input [7:0] a; 
	input [7:0] b; 
	input cin;
	output [7:0] sout; 
	output cout;
	wire c0, c1, c2;
	
	cla n1(a[1:0], b[1:0], cin, c0, sout[1:0]);
	cla n2(a[3:2], b[3:2], c0, c1, sout[3:2]);
	cla n3(a[5:4], b[5:4], c1, c2, sout[5:4]);
	cla n4(a[7:6], b[7:6], c2, cout, sout[7:6]);
	
	
endmodule
