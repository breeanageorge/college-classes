`include "eightbitcla.v"
`include "inverter.v"
module twoaddsub(a, b, m, sum, ovf);
	input [7:0] a; 
	input [7:0] b; 
	input m;
	output [7:0] sum; 
	output ovf;
	wire [7:0] b1;
	
	inverter j1(m, b, b1);
	eightbitcla n1(a, b1, m, sum, ovf);
	
endmodule
