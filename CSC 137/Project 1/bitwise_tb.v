`include "bitwise.v"
module bitwise_tb();
	reg x, y, z;
	
	reg [3:0] k;
	wire f;
	
	bitwise bit_test(x, y, z, f);
	
	initial begin //start test
	$display("Time x y z f"); //header
	$monitor ("%4d       %b", $time, f);
	
	for(k=0; k<=7; k=k+1) begin
		#1 x=k[2];y=k[1];z=k[0]; 
		$display("%4d %b %b %b", $time, x, y, z);
	end
	#10 $finish; //stops simulation
	end

endmodule
