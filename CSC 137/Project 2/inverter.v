module inverter(m, bin, bout);
input [7:0] bin; 
input m;
reg [8:0] i;
output reg [7:0] bout;

always @(m) begin
  for (i=0; i<8;i=i+1) begin
  if (bin[i] == 0) begin
    bout[i] = 1;
  end else begin
    bout[i] = 0;
  end
  end
end
endmodule