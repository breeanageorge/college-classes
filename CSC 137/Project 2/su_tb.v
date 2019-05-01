`include "pgu_su.v"
module pgu_su_tb();
	reg a, b, cin, sout;
	
	reg [3:0] k;
	wire gout, sout;
	
	pgu_su pgu_su_test(a, b, cin, pout, gout, sout);
	
	initial begin //start test
	$display("Time cin pout sout"); //header
	$monitor ("%4d       %b", $time, sout);
	
	for(k=0; k<=3; k=k+1) begin
		#1 cin=k[1];pout=k[0]; 
		$display("%4d %b %b", $time, cin, pout);
	end
	#10 $finish; //stops simulation
	end
	
endmodule
