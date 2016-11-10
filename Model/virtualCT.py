import numpy as np
import dicom
from dicom.dataset import Dataset, FileDataset
import matplotlib.pyplot as plt
import matplotlib.colors as colors
import random
from datetime import datetime
from sys import argv

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
	omin=densities.min(); omax=densities.max(); nmin=-100; nmax=6500
	densities=np.array([[ (d-omin)*float(nmax-nmin)/(omax-omin)+nmin for d in da] for da in densities])
	densities+=0.5*noiseAmp*np.random.randn(*densities.shape)
	return densities

def fileOutputTest(infilename,PLOT=False):
	noiseAmp=150#units are "HA". See file in slack.
	cellLocations=read_CA_output(infilename,noiseAmp)
	outfilename=datetime.now().strftime("%y-%m-%d-%H%M.dcm")

	# Populate required values for file meta information
	file_meta = Dataset()
	file_meta.MediaStorageSOPClassUID = '1.2.840.10008.5.1.4.1.1.2'  # CT Image Storage
	file_meta.MediaStorageSOPInstanceUID = "1.2.3"  # !! Need valid UID here for real work
	file_meta.ImplementationClassUID = "1.2.3.4"  # !!! Need valid UIDs here
	# Create the FileDataset instance and set file_meta
	ds = FileDataset(outfilename, {}, file_meta=file_meta, preamble="\0" * 128)
	
	# Set the image's data equal to that from our CA output
	ds.PixelData = cellLocations
	ds.Columns = cellLocations.shape[0]
	ds.Rows = cellLocations.shape[1]
	ds.PatientName = "Test^Firstname"
	ds.PatientID = "123456" #n.b. more required from DICOM standard

	# Set the transfer syntax
	ds.is_little_endian = True
	ds.is_implicit_VR = True

	# Imaging components
	ds.SamplesPerPixel = 1
	ds.PhotometricInterpretation = "MONOCHROME2"
	ds.PixelRepresentation = 0
	ds.HighBit = 15
	ds.BitsStored = 16
	ds.BitsAllocated = 16
	ds.SmallestImagePixelValue = '\\x00\\x00'
	ds.LargestImagePixelValue = '\\xff\\xff'

	ds.save_as(outfilename)

	if(PLOT):
		plt.imshow(ds.PixelData,cmap=plt.cm.bone); plt.show()

if len(argv)>1: fileOutputTest(argv[1],PLOT=True)
else: print "Please provide a filename."