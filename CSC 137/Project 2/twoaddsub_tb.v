`include "twoaddsub.v"
module twoaddsub_tb();
	reg[7:0] a; 
	reg [7:0] b; 
	reg m;
	
	reg [7:0] k;
	wire [7:0] sum;
	wire ovf;
	
	twoaddsub twoaddsub_test(a, b, m, sum, ovf);
	
	initial begin
	$display("Time a b m sum ovf"); //header
	$monitor ("%4d       %b", $time, sum, ovf);
	
	for(k=0; k<=7; k=k+1) begin
		#1 a=k[2];b=k[1];m=k[0]; 
		$display("%4d %b %b", $time, sum, ovf);
	end
	#10 $finish; //stops simulation
	end

endmodule
