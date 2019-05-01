`include "cgu.v"
module cgu_tb();
	reg pout, gout, cin;
	
	reg [7:0] k;
	wire cout;
	
	cgu cgu_test(pout, cin, gout, cout);
	
	initial begin //start test
	$display("Time cin pout gout cout"); //header
	$monitor ("%4d       %b", $time, cout);
	
	for(k=0; k<=7; k=k+1) begin
		#1 cin=k[2];pout=k[1];gout=k[0]; 
		$display("%4d %b %b %b", $time, cin, pout, gout);
	end
	#10 $finish; //stops simulation
	end

endmodule
