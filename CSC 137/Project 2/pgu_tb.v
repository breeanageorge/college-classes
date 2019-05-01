`include "pgu_su.v"
module pgu_su_tb();
	reg a, b, cin;
	
	reg [3:0] k;
	wire gout, sout, pout;
	
	pgu_su pgu_su_test(a, b, cin, pout, gout, sout);
	
	initial begin //start test
	$display("Time a b pout gout"); //header
	$monitor ("%4d       %b", $time, gout);
	
	for(k=0; k<=3; k=k+1) begin
		#1 a=k[1];b=k[0]; 
		$display("%4d %b %b", $time, a, b);
	end
	#10 $finish; //stops simulation
	end
	
endmodule
