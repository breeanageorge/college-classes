`include "pgu_su.v"
`include "cgu.v"
module cla(a, b, cin, cout, sout);
	input [1:0] a; 
	input [1:0] b;
	input cin;
	output [1:0] sout;
	output cout;
	wire [1:0] pout;
	wire [1:0] gout;
	wire c0;
	
	pgu_su pgu_su1(a, b, cin, c0, pout, gout, sout);
	cgu cgu_1(pout, cin, c0, gout, cout);	

endmodule
