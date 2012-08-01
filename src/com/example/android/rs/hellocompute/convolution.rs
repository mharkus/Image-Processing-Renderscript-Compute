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
	const float filterMatrix[9] = { 
		-1, -2, -1,
		0, 0, 0,
		1, 2, 1
	};	
	
    int32_t row_index = *v_in;
    int filterWidth = 3;
    int filterHeight = 3;
    	float4 vals[9];
        double red = 0.0, green = 0.0, blue = 0.0;

		for(int x = 0; x < mImageWidth; x++){
		
			float4 current = rsUnpackColor8888(gInPixels[x + row_index]);
			
			vals[0] = rsUnpackColor8888(gInPixels[x - 1 + row_index - 1]);
			vals[1] = rsUnpackColor8888(gInPixels[x + row_index - 1]);
			vals[2] = rsUnpackColor8888(gInPixels[x + 1 + row_index - 1]);
			
			vals[3] = rsUnpackColor8888(gInPixels[x - 1 + row_index]);
			vals[4] = rsUnpackColor8888(gInPixels[x + row_index]);
			vals[5] = rsUnpackColor8888(gInPixels[x + 1 + row_index]);
			
			vals[6] = rsUnpackColor8888(gInPixels[x - 1 + row_index + 1]);
			vals[7] = rsUnpackColor8888(gInPixels[x + row_index + 1]);
			vals[8] = rsUnpackColor8888(gInPixels[x + 1 + row_index + 1]);
			
			red =   vals[0].r * filterMatrix[0] +
					vals[1].r * filterMatrix[1] +  
					vals[2].r * filterMatrix[2] +
					vals[3].r * filterMatrix[3] +
					vals[4].r * filterMatrix[4] +
					vals[5].r * filterMatrix[5] +
					vals[6].r * filterMatrix[6] +
					vals[7].r * filterMatrix[7] +
					vals[8].r * filterMatrix[8];
					
			green = vals[0].g * filterMatrix[0] +
					vals[1].g * filterMatrix[1] +  
					vals[2].g * filterMatrix[2] +
					vals[3].g * filterMatrix[3] +
					vals[4].g * filterMatrix[4] +
					vals[5].g * filterMatrix[5] +
					vals[6].g * filterMatrix[6] +
					vals[7].g * filterMatrix[7] +
					vals[8].g * filterMatrix[8];
			
			blue = vals[0].b * filterMatrix[0] +
					vals[1].b * filterMatrix[1] +  
					vals[2].b * filterMatrix[2] +
					vals[3].b * filterMatrix[3] +
					vals[4].b * filterMatrix[4] +
					vals[5].b * filterMatrix[5] +
					vals[6].b * filterMatrix[6] +
					vals[7].b * filterMatrix[7] +
					vals[8].b * filterMatrix[8];
					
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

