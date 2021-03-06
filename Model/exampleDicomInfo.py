def setAttributes(df):
	# df.ProcedureCodeSequence= [(0008, 0100) Code Value                          SH: 'PX012'+'(0008=, 0102) Coding Scheme Designator            SH: 'GEIIS''+'(0008=, 0103) Coding Scheme Version               SH: '0''+'(0008=, 0104) Code Meaning                        LO: 'PET CT Merck ovarian']'
	# df.ReferencedImageSequence= '[(0008, 1150) Referenced SOP Class UID            UI: CT Image Storage'+'(0008=, 1155) Referenced SOP Instance UID         UI: 1.2.840.113619.2.55.3.2584397314.242.1262073083.285.1]'
	# df.ReferencedPatientSequence= '[(0008, 1150) Referenced SOP Class UID            UI: Detached Patient Management SOP Class'+'(0008=, 1155) Referenced SOP Instance UID         UI: 1.2.124.113532.10.154.2.34.20050713.102618.621503]'
	# df.ReferencedStudySequence= '[(0008, 1150) Referenced SOP Class UID            UI: Detached Study Management SOP Class'+'(0008=, 1155) Referenced SOP Instance UID         UI: 1.2.124.113532.10.154.2.34.20091222.171457.5267078]'
	# df.RequestAttributesSequence= '[(0040, 1001) Requested Procedure ID              SH: 'X-14515838']'
	df.AccessionNumber= 'X-14515838'
	df.AcquisitionDate= '20091229'
	df.AcquisitionNumber= 1
	df.AcquisitionTime= '101747'
	df.AdditionalPatientHistory= ''
	df.BitsAllocated= 16
	df.BitsStored= 12
	# df.BitsStored= 16 #Charlie
	df.ContentDate= '20091229'
	df.ContentTime= '101958'
	df.ConvolutionKernel= 'STANDARD'
	df.DataCollectionDiameter= 500.000000
	df.DistanceSourceToDetector= 949.075012
	df.DistanceSourceToPatient= 541.000000
	df.EthnicGroup= ''
	df.Exposure= 0
	df.ExposureTime= 500
	df.FillerOrderNumberImagingServiceRequest= 'E-04344684'
	df.FilterType= 'BODY FILTER'
	df.FocalSpots= '0.700000'
	# df.FrameOfReferenceUID= '1.2.840.113619.2.55.3.2584397314.242.1262073083.283.6315.1'  #Charlie
	df.FrameOfReferenceUID= '1.3.6.1.4.1.9590.100.1.2.209731216212672211816830864861240885076'
	df.GantryDetectorTilt= '0.000000'
	df.GeneratorPower= 3500
	df.GroupLength=912
	df.HighBit= 11
	# df.HighBit= 15 #Charlie
	df.ImageOrientationPatient= ['1.0', '0.0', '0.0', '0.0', '1.0', '0.0']
	# df.ImageOrientationPatient= ['1.000000', '0.000000', '0.000000', '0.000000', '1.000000', '0.000000'] #Charlie
	# df.ImagePositionPatient= ['-250.000', '-250.000', '-126.680']  #Charlie
	df.ImagePositionPatient= ['1.0', '-511.0', '-0.0']
	df.ImageType= ['ORIGINAL', 'PRIMARY', 'AXIAL']
	# df.InstanceCreationDate= '20091229'  #Charlie
	df.InstanceCreationDate= '20150514'
	# df.InstanceCreationTime= '101958'  #Charlie
	df.InstanceCreationTime= '171215.999'
	df.InstanceNumber= 1
	# df.InstanceNumber= 10 #Charlie
	df.InstitutionName= 'CERR'
	df.IssuerOfPatientID= 'HOSP'
	df.KVP= 140
	# df.Manufacturer= 'GE MEDICAL SYSTEMS' #Charlie
	df.Manufacturer= 'Picker International, Inc. PQ2000'
	# df.ManufacturerModelName= 'Discovery 690' #Charlie
	df.ManufacturerModelName= 'CERR'
	df.Modality= 'CT'
	df.NameOfPhysiciansReadingStudy= ''
	df.OperatorsName= 'SB'
	df.PatientAge= '053Y'
	df.PatientID= '1.3.6.1.4.1.9590.100.1.2.18681224811319900718684246042696479032'
	df.PatientName= 'HNTCP5'
	df.PatientPosition= 'HFS'
	df.PatientSex= 'F'
	df.PatientSize= 1.54
	df.PatientWeight= 70.0
	df.PerformedProcedureStepDescription= 'PET CT Merck ovarian'
	df.PerformedProcedureStepID= 'PPS ID    739'
	df.PerformedProcedureStepStartDate= '20091229'
	df.PerformedProcedureStepStartTime= '101625'
	df.PhotometricInterpretation= 'MONOCHROME2'
	df.PixelPaddingValue= '-2000'
	df.PixelRepresentation= 0
	df.PixelRepresentation= 1
	# df.PixelSpacing= ['0.976562', '0.976562'] #Charlie
	df.PixelSpacing= ['2.0', '2.0']
	df.PositionReferenceIndicator= 'XY'
	df.ProtocolName= '5.17 ADDPET12 Ovarian Merck'
	df.ReconstructionDiameter= '500.000000'
	df.ReferringPhysicianName= 'BRENTON^JD'
	df.RequestingService= 'CLINICAL ONCOLOGY'
	df.RescaleIntercept= -1024.0
	df.RescaleSlope= 1.0
	df.RescaleType= 'HU'
	df.RevolutionTime= 0.5
	df.RotationDirection= 'CW'
	df.SamplesPerPixel= 1
	df.ScanOptions= 'HELICAL MODE'
	df.SeriesDate= '20091229'
	df.SeriesDescription= 'CT Std'
	df.SeriesInstanceUID= '1.3.6.1.4.1.9590.100.1.2.31285477210144452537130684751254926540'
	df.SeriesNumber= 3
	df.SeriesTime= '101734'
	df.SingleCollimationWidth= 0.625
	df.SliceLocation= -126.680
	df.SliceThickness= 2.5
	# df.SliceThickness= 3.750000 #Charlie
	df.SoftwareVersions= 'kh_xar.30'
	df.SOPClassUID= 'CT Image Storage'
	# df.SOPInstanceUID= '1.2.840.113619.2.55.3.2584397314.242.1262073083.550.10' #Charlie
	df.SOPInstanceUID= '1.3.6.1.4.1.9590.100.1.2.115051905812620145920373971792360190180'
	df.SpecificCharacterSet= 'ISO_IR 100'
	df.SpiralPitchFactor= 1.375
	# df.StationName= 'pet2' #Charlie
	df.StationName= 'CERR'
	df.StudyDate= '20091229'
	df.StudyDescription= 'PET CT Merck ovarian'
	df.StudyID= '739'
	df.StudyInstanceUID= '1.3.6.1.4.1.9590.100.1.2.350145614110450954838285024532024857158'
	df.StudyTime= '101625'
	df.TableFeedPerRotation= 55.0
	df.TableHeight= 159.500000
	df.TableSpeed= 110.0
	df.TotalCollimationWidth= 40.0
	df.WindowCenter= 40
	df.WindowWidth= 400
	df.XRayTubeCurrent= 25