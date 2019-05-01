module mealy_seq(
input clock, reset, x,
output reg z
);

parameter	A = 2'b00,
			B = 2'b01,
			C = 2'b10,
			D = 2'b11;

reg [1:0] current_state, next_state;			

always@(*)
begin
	case (current_state)
	A:	if (x==1) begin	
			next_state = B;
			z = 0;
		end
		else begin
			next_state = A;
			z = 0;
		end
	B:	if (x==1) begin	
			next_state = B;
			z = 0;
		end
		else begin
			next_state = C;
			z = 0;
		end
	C:	if (x==1) begin	
			next_state = B;
			z = 0;
		end
		else begin
			next_state = D;
			z = 0;
		end
	D:	if (x==1) begin	
			next_state = B;
			z = 1;
		end
		else begin
			next_state = A;
			z = 0;
		end
	default: next_state = 2'bxx;
	endcase
	end
	
	always@(posedge clock)
	begin
		if (reset == 1)
			current_state <= A;
		else
			current_state <= next_state;
	end		
	
endmodule