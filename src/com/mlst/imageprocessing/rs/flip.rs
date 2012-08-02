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
int width;
int height;
int direction = 0; // 0 - flip horizontally, 1 - flip vertically

void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {
	    
    if(direction == 0){ // flip horiz
    	const uchar4 *element = rsGetElementAt(gIn, width - x, y);
		float4 color = rsUnpackColor8888(*element);
    	float4 output = {color.r, color.g, color.b};
    	*v_out = rsPackColorTo8888(output);
    }else if(direction == 1){ // flip vert
    	const uchar4 *element = rsGetElementAt(gIn, x, height - y);
		float4 color = rsUnpackColor8888(*element);
    	float4 output = {color.r, color.g, color.b};
    	*v_out = rsPackColorTo8888(output);
    }else if(direction == 2){ // rotate left
    	const uchar4 *element = rsGetElementAt(gIn, width - y, x);
		float4 color = rsUnpackColor8888(*element);
    	float4 output = {color.r, color.g, color.b};
    	*v_out = rsPackColorTo8888(output);
    }else if(direction == 3){ // rotate right
    	const uchar4 *element = rsGetElementAt(gIn, y, height - x);
		float4 color = rsUnpackColor8888(*element);
    	float4 output = {color.r, color.g, color.b};
    	*v_out = rsPackColorTo8888(output);
    }
   	
    
}

void filter() {
    rsForEach(gScript, gIn, gOut, 0);
    
}

