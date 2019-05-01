`include "xorgate.v"
module xorgate_tb();
	reg x, y;
	
	reg [3:0] k;
	wire f;
	
	xorgate xor_test(x, y, f);
	
	initial begin //start test
	$display("Time x y f"); //header
	$monitor ("%4d       %b", $time, f);
	
	for(k=0; k<=3; k=k+1) begin
		#1 x=k[1];y=k[0]; 
		$display("%4d %b %b", $time, x, y);
	end
	#10 $finish; //stops simulation
	end

endmodule
