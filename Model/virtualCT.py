from sys import byteorder
import numpy as np
import dicom
# from dicom import read_file,
from dicom.dataset import Dataset, FileDataset
import matplotlib.pyplot as plt
import matplotlib.colors as colors
import random
from datetime import datetime
from sys import argv
import struct
from exampleDicomInfo import setAttributes
nmin=-100; nmax=6500

def read_CA_output(filename,noiseAmp):
	#Define values
	densities=np.array([]); xRange=0
	#Read densities from file
	fileIn=open(filename,"rU")
	for i,line in enumerate(fileIn):
		cols=line.strip().split(" ")
		xRange=len(cols)
		if(i==0): densities=np.zeros([xRange,xRange])
		elif i==len(densities): densities.resize([2*len(densities),xRange])
		densities[i,:]=cols
	fileIn.close()
	densities.resize(i,xRange)
	omin=densities.min(); omax=densities.max(); 
	densities=np.array([[ (d-omin)*float(nmax-nmin)/(omax-omin)+nmin for d in da] for da in densities])
	densities+=0.5*noiseAmp*np.random.randn(*densities.shape)
	return densities

def fileOutput(infilename,PLOT=False):
	noiseAmp=150#units are "HA". See file in slack.
	cellLocations=read_CA_output(infilename,noiseAmp)
	outfilename=datetime.now().strftime("%y-%m-%d-%H%M.dcm")

	# Populate required values for file meta information
	file_meta = Dataset()
	file_meta.FileMetaInformationGroupLength=190
	file_meta.FileMetaInformationVersion='\x00\x01'
	file_meta.MediaStorageSOPClassUID = '1.2.840.10008.5.1.4.1.1.2'  # CT Image Storage
	file_meta.MediaStorageSOPInstanceUID = '1.3.6.1.4.1.9590.100.1.2.115051905812620145920373971792360190180'
	file_meta.TransferSyntaxUID = '1.2.840.10008.1.2'
	file_meta.ImplementationClassUID = '1.2.40.0.13.1.1'
	file_meta.ImplementationVersionName= 'dcm4che-2.0'
	ds = FileDataset(outfilename, {}, file_meta=file_meta, preamble="\0" * 128)
	# Set the transfer syntax
	ds.is_little_endian = True
	ds.is_implicit_VR = True
	#Set the data that we copied from another dicom file
	setAttributes(ds)

	# Set the image's data equal to that from our CA output
	ds.Rows = cellLocations.shape[0]
	ds.Columns = cellLocations.shape[1]
	if cellLocations.dtype != np.uint16:
		ds.PixelData = cellLocations.astype(np.uint16).tostring()
	else: ds.PixelData = cellLocations.tostring()
	ds.SmallestImagePixelValue = struct.pack("<i",nmin) #'\\x00\\x00': struct.pack("<H",0), w/ little_endian=True https://docs.python.org/2/library/struct.html#byte-order-size-and-alignment
	ds.LargestImagePixelValue = struct.pack("<i",nmax) #'\\xff\\xff': struct.pack("<H",65535)

	ds.save_as(outfilename)
	if(PLOT):
		plt.imshow(ds.pixel_array,cmap=plt.cm.bone); plt.show()

if len(argv)>1: fileOutput(argv[1],PLOT=True)
else: print "Please provide a filename."