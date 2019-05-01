`include "twoaddsub.v"
module twoaddsub_tb();
	reg a, b, m;
	
	reg [7:0] k;
	wire sum, ovf;
	
	twoaddsub twoaddsub_test(a, b, m, sum, ovf);
	
	initial begin
	$display("Time a b m sum ovf"); //header
	$monitor ("%4d       %b", $time, sum, ovf);
	
	#1 a="8’hFF";b="8’h01";m="1’b0"; 
	$display("%4d %b %b", $time, sum, ovf);
	
	#1 a="8’h7F";b="8’h01";m="1’b0"; 
	$display("%4d %b %b", $time, sum, ovf);
	
	#1 a="8’h01";b="8’hFF";m="1’b0"; 
	$display("%4d %b %b", $time, sum, ovf);
	
	#1 a="8’h55";b="8’hAA;";m="1’b0"; 
	$display("%4d %b %b", $time, sum, ovf);
	
	#1 a="8’h80";b="8’h01";m="1’b1"; 
	$display("%4d %b %b", $time, sum, ovf);
	
	#1 a="8’h6C";b="8’hCA";m="1’b1"; 
	$display("%4d %b %b", $time, sum, ovf);
		
	#10 $finish; //stops simulation
	end

endmodule
