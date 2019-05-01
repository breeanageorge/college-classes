module cgu(pout, cin, gout, cout, c0);
	input [1:0] pout; 
	input [1:0] gout;
	input cin;
	output cout;
	output c0;
	
	wire np0, np1, ncin, n3, n4, n7, n8;
	
	not ncin1(ncin, cin);
	not npout0(np0, pout[0]);
	not npout1(np1, pout[1]);
	
	nor nor3(n3, ncin, np0);
	nor nor4(n4, n3, gout[0]);
	
	nor nor7(n7, n4, np1);
	nor nor8(n8, n7, gout[1]);
	
	not nc0(c0, n4);
	not ncout(cout, n8);
	
endmodule
