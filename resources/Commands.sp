duplicates:
	help =
		Scan directories to search for duplicates files according the given comparators (see option --comp).
		The result of the analysis will be output into a report file.
	properties:
		files:
			help = Files and directories to analyze (all sub-files will be analyzed).
			count = 1 -1
		comp:
			help =
				Define the comparators to use. The resulting analyzed files will be considered duplicates if
				stated as identical for all given comparators. Note, the order of the comparators may affect
				the process speed. It is recommended to specify fast comparators first.
				The comparators can be $1.
			count = 1 -1
			markers = --comp
			default = size name
		min_duplicates:
			help =
				Minimum number of duplicates to consider. In other words, files that are duplicated this
				number of times will be shown in the report.
			count = 1 1
			markers = --min-dup
			type = integer
			default = 2
		min_size:
			help =
				Minimum file size (in KB) to consider. All files of size less than this size will ignored.
				If this value is 0 or less, no minimum size filter will apply.
			count = 1 1
			markers = --min-size
			type = integer
			default = 1
		max_size:
			help =
				Maximum file size (in KB) to consider. All files of size bigger than this size will ignored.
				If this value is 0 or less, no maximum size filter will apply.
			markers = --max-size
			type = integer
			default = 0
		checksum_max_iterations:
			help =
				If a checksum comparator is used, this value will set a limit number of steps for the
				checksum computation. In other words, if a file is too large, only a partial checksum will be
				computed. This can be used to limit the computation on large files while keeping them in the
				report as "potentially" equal.

				More precisely, the maximum number of bytes computed will be:
						checksum_max_depth * checksum_size * checksum_step

				Warning, if a limit is set, two large files might be considered equal where in fact, the last
				bytes (beyond the set limit) might differ.

				If this value is set to 0 or less, no limit will be set and checksum will be computed in the
				entirety if needed.
			count = 1 1
			markers = --cs-max-depth
			type = integer
			default = -1
		checksum_size:
			help =
				If a checksum comparator is used, this value defines the number of bytes of the checksum.
			count = 1 1
			markers = --cs-size
			type = integer
			default = $2
		checksum_step:
			help =
				If a checksum comparator is used, this value defines the number of iteration for each step.
				This program does not necessarily compute the checksum over the entire files. In fact, when
				comparing files, it incrementally computes the partial checksum until they differ or the
				entire file has been processed. Then, at each incremental step, the number of bytes processed
				for the checksum can be computed by:
						checksum_size * checksum_step
			count = 1 1
			markers = --cs-step
			type = integer
			default = $3

