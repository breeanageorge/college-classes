`include "xorgate.v"
module pgu_su(a, b, cin, c0, pout, gout, sout);
	input [1:0] a; 
	input [1:0] b; 
	input cin, c0;
	output [1:0] pout;
	output [1:0] gout; 
	output [1:0] sout;

		

	xorgate xorpgu0(a[0], b[0], pout[0]);
	and andpgu0(gout[0], a[0], b[0]);
	xorgate xorpgu1(a[1], b[1], pout[1]);
	and andpgu1(gout[1], a[1], b[1]);
	
	
	xorgate xorsu0(pout[0], cin, sout[0]);
	xorgate xorsu1(pout[1], c0, sout[1]);
	
endmodule
