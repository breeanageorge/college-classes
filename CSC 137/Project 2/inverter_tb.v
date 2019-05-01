`include "inverter.v"
module inverter_tb();
	reg [7:0] y;
	
	reg [7:0] k;
	wire [7:0] ynot;
	
	inverter inverter_test(y, ynot);
	
	initial begin //start test
	$display("Time y ynot"); //header
	$monitor ("%4d       %b", $time, ynot);
	
	for(k=0; k<=7; k=k+1) begin
		#1 y=k[0]; 
		$display("%4d %b", $time, ynot);
	end
	#10 $finish; //stops simulation
	end

endmodule
