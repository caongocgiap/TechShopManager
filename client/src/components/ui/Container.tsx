const Container = ({
  children,
  className,
}: {
  children: React.ReactNode;
  className?: string;
}) => {
  return (
    <div
      className={`mx-auto max-w-full h-full ${className ? className : null}`}
    >
      {children}
    </div>
  );
};

export default Container;
