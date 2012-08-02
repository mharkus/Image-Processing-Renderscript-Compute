/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#pragma version(1)
#pragma rs java_package_name(com.example.android.rs.hellocompute)

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

int mImageWidth;
int mImageHeight;
const uchar4 *gInPixels;
uchar4 *gOutPixels; 

float factor;
float bias;



void root(const int32_t *v_in, int32_t  *v_out, const void *usrData, uint32_t x, uint32_t y) {
	const float filterMatrix[] = { 
		-1,  0,  0,  0,  0,
     0, -2,  0,  0,  0,
     0,  0,  6,  0,  0,
     0,  0,  0, -2,  0,
     0,  0,  0,  0, -1,
		
	};	
	
    int32_t row_index = *v_in;
    
    float4 vals[9];
    
    
    int matrixWidth = 5;
   	int matrixHeight = matrixWidth;
    int diff = (int) (-matrixWidth/2);

		for(int x = 0; x < mImageWidth; x++){
			int col = diff, row = diff;
			double red = 0.0, green = 0.0, blue = 0.0;
		
			int ctr = 0;   
		    for(int i=0; i < matrixWidth; i++){
			   for(int j=0; j < matrixHeight; j++){
			   		vals[ctr] = rsUnpackColor8888(gInPixels[x + row_index + row + col++]);
			   		
			   		red += (vals[ctr].r * filterMatrix[ctr]);
			   		green += (vals[ctr].g * filterMatrix[ctr]);
			   		blue += (vals[ctr].b * filterMatrix[ctr]); 
			   		
			   		ctr++;
			   }
			
			   col = diff;
			   row++;
		    
		    }
		    
		    // apply our factor to increase/reduce the degree of effect 
		    // and our bias to control the brightness of our image
		    
			red = red + red * factor + bias;
			green = green + green * factor + bias;
			blue = blue + blue * factor + bias;		
					
			red = red > 1 ? 1 : red;
			green = green > 1 ? 1 : green;
			blue = blue > 1 ? 1 : blue;
		
			float3 output = {red, green, blue};
    		gOutPixels[x + row_index] = rsPackColorTo8888(output);
    		
			
    	}
    	
    	
    	 
		
	
}

void filter() {
    rsForEach(gScript, gIn, gOut, 0);
    
}

