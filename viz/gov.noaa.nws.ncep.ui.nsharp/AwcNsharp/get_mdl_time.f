	SUBROUTINE GET_MDL_TIMES ( snfile, time_list, ntimf )
C************************************************************************
C* SNLIST								*
C*									*
C* This program lists data from a sounding dataset.			*
C*									*
C* Log:									*
C* I. Graffman/RDS	 8/87						*
C* M. desJardins/GSFC	10/88	Rewritten				*
C* M. desJardins/GSFC	 4/89	Modify to list unmerged data		*
C* S. Schotz/GSC	 8/90	Corrected bogus error message for 	*
C*				unmerged listing			*
C* J. Whistler/SSAI	 5/91	Changed output*20 to output*48		*
C* S. Jacobs/NMC	 6/94	STNDEX*48 --> *72			*
C* L. Williams/EAI	 7/94	Removed call to SNLUPD			*
C* S. Jacobs/NMC	 3/95	Changed call to SNLLEV to pass file num	*
C* L. Hinson/AWC        11/02   Modified dimensions of time_list to     *
C*                              match that of call from xwvid6.c -      *
C*                                 mtime_list                           *
C************************************************************************
	INCLUDE 	'GEMPRM.PRM'
C*
	CHARACTER*(*)	snfile
	CHARACTER	time_list(500)*20
	CHARACTER	lastfil*200
	CHARACTER	lasttm*20
	REAL		anl(LLNANL), rnav(LLNNAV)
C------------------------------------------------------------------------
C*	Initialize user interface.
C
	CALL IN_BDTA  ( ier )
	CALL GG_INIT ( 1, ier )
C
C*	Open the input file.
C
        lastfil = ' '
        CALL GR_OPEN  ( snfile, .false., lastfil, igdfln, lasttm,
     +		        anl, rnav, numgrd, maxgrd, newfile, ier )
C
C*	Get input times and pointers.
C
	CALL GD_GTIM  ( igdfln, LLMXTM, time_list, ntimf, ier )
C
	CALL GD_CLOS  ( igdfln, iret )
C*
	END
